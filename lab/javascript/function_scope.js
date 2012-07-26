var global = "global"

function scopeDemo(){
    console.log("in func, before declare:" + global) // the variable will be hoisted at the top of the function body, but keep the value 'undefined', until the actual 'var global' is hit, the value will not be assigned.
    var global = "local"
    console.log("in func, after decalre:" + global)
}

scopeDemo()
