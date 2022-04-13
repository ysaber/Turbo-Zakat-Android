package com.nzf.turbozakat.equationparams

data class PropertyParams(var isItOwned: Boolean = true,
                          var monthlyRent: Double = 0.0,
                          var salePrice: Double = 0.0,
                          var monthlyMaintenance: Double = 0.0,
                          var annualPropertyTax: Double = 0.0,
                          var monthlyMiscExpenses: Double = 0.0,
                          override var label: String = ""
) : EquationParams