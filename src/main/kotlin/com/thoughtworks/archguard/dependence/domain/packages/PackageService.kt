package com.thoughtworks.archguard.dependence.domain.packages

import com.thoughtworks.archguard.dependence.domain.base_module.BaseModuleRepository
import com.thoughtworks.archguard.dependence.infrastructure.packages.PackageDependenceDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PackageService {
    @Autowired
    lateinit var packageRepository: PackageRepository

    @Autowired
    lateinit var moduleRepository: BaseModuleRepository


    fun getPackageDependence(): PackageGraph {
        val results = packageRepository.getPackageDependence()
        return getPackageGraph(results)
    }

    private fun getPackageGraph(results: List<PackageDependenceDTO>): PackageGraph {
        val packageStore = PackageStore()
        results.forEach {
            it.aClz = it.aClz.substringBeforeLast('.')
            it.bClz = it.bClz.substringBeforeLast('.')
        }
        results.filter {
            !it.aClz.contains("$")
                    && !it.aClz.contains("[")
                    && !it.bClz.contains("$")
                    && !it.bClz.contains("[")
        }
                .groupBy { it.aClz }
                .forEach {
                    it.value.groupBy { i -> i.bClz }
                            .forEach { i -> packageStore.addEdge(it.key, i.key, i.value.size) }
                }

        return packageStore.getPackageGraph()
    }

    fun getPackageDependencies(): List<ModulePackage> {
        return moduleRepository.getBaseModules().map {
            val dependencies = packageRepository.getPackageDependenceByModule(it)
            ModulePackage(it, getPackageGraph(dependencies))
        }
    }
}
