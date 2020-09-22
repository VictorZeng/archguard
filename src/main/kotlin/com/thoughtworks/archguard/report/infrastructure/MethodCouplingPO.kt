package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.coupling.MethodCoupling

data class MethodCouplingPO(val id: String, val moduleName: String? = null,
                            val classFullName: String, val methodName: String, val args: String?, val fanIn: Int, val fanOut: Int) {
    fun toMethodCoupling(): MethodCoupling {
        var coupling = 1 - 1.0 / (fanIn + fanOut)
        if (coupling.isInfinite() || coupling.isNaN()) {
            coupling = 0.0
        }
        var instability = fanOut.toDouble() / (fanIn + fanOut)
        if (instability.isInfinite() || instability.isNaN()) {
            instability = 0.0
        }
        return MethodCoupling(id, moduleName, getPackageName(classFullName), getClassName(classFullName), methodName, args, fanIn, fanOut, coupling, instability)
    }
}
