package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.util.stream.Collectors

class GitHotFileScannerTool(val systemRoot: File, val branch: String) {

    private val log = LoggerFactory.getLogger(GitHotFileScannerTool::class.java)

    fun getGitHotFileReport(): File? {
        val reportPath = systemRoot.toString() + "/git_hot_file.txt"
        scan(listOf("git", "log", branch, "--no-merges", "--name-only", "--oneline", "--pretty=format:"), reportPath)
        val report = File(reportPath)
        return if (report.exists()) {
            report
        } else {
            log.info("failed to get git_hot_file.txt")
            null
        }
    }

    private fun scan(cmd: List<String>, reportPath: String) {
        Processor.executeWithLogsAndAppendToFile(ProcessBuilder(cmd), systemRoot, reportPath)
    }

    fun getGitHotFileModifiedCountMap(): Map<String, Int> {
        if (getGitHotFileReport() == null) return mapOf()
        val map = mutableMapOf<String, Int>();
        getGitHotFileReport()!!.readLines().stream()
                .map { it.replace("\n", "").replace("\r", "") }
                .filter { it != "" }
                .forEach {
                    if (map.contains(it)) {
                        map[it] = map[it]!! + 1
                    } else {
                        map[it] = 1
                    }
                }
        return map
    }
}
