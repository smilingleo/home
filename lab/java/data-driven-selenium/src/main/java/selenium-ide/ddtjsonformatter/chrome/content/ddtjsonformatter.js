const JSONFORMATTER_ID = 'ddtjsonformatter@zuora.com';

function initializeJsonformatterObserver()
{
    jsonformatterObserver.register();
}

var jsonformatterObserver =
{
    _uninstall: false,
    observe: function(subject, topic, data)
    {
        if(topic == "em-action-requested")
        {
            subject.QueryInterface(Components.interfaces.nsIUpdateItem);
            if(subject.id == JSONFORMATTER_ID)
            {
                if(data == "item-uninstalled")
                {
                    this._uninstall = true;
                }
                else if(data == "item-disabled")
                {
                    this._uninstall = true;
                }
                else if(data == "item-cancel-action")
                {
                    this._uninstall = false;
                }
            }
        }
        else if(topic == "quit-application-granted")
        {
            if(this._uninstall)
            { // your uninstall stuff goes here

                var branch = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefService).getBranch("extensions.selenium-ide.");

                // this section removes the formatter we added
                var current_ppf = branch.getCharPref("pluginProvidedFormatters");
                if(typeof current_ppf != "undefined")
                {
                    var split_ppf = current_ppf.split(",");
                    for (var ppf = 0; ppf < split_ppf.length; ppf++)
                    {
                        if(split_ppf[ppf].search("chrome://ddtjsonformatter/content/formats/ddtjson-formatter.js") != -1)
                        {
                            branch.setCharPref("pluginProvidedFormatters", current_ppf.replace(split_ppf[ppf], ""));
                        }
                    }
                }

                // Version 1.1 & inferior fix:
                branch.deleteBranch('ddtjsonformatter'); // remove custom branch

                // Remove custom branch
                branch.deleteBranch('formats.ddtjsonformatter'); // remove custom branch
            }
            this.unregister();
        }
    }, register: function()
    {
        var observerService = Components.classes["@mozilla.org/observer-service;1"].getService(Components.interfaces.nsIObserverService);
        observerService.addObserver(this, "em-action-requested", false);
        observerService.addObserver(this, "quit-application-granted", false);
    }, unregister: function()
    {
        var observerService = Components.classes["@mozilla.org/observer-service;1"].getService(Components.interfaces.nsIObserverService);
        observerService.removeObserver(this, "em-action-requested");
        observerService.removeObserver(this, "quit-application-granted");
    }
}

window.addEventListener("load", initializeJsonformatterObserver, false);