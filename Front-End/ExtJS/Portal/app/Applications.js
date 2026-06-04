Ext.define('Portal.Application', {
    extend:'Ext.app.Application',
    name:  'Portal',

    views:
    [
        'Portal.view.login.Login',
        'Portal.view.main.Main'
    ],

    removeSplash: function()
    {
        Ext.getBody().removeCls('launching');
        var esplash = document.getElementById("splash");
        esplash.parentNode.removeChild(esplash);
    },

    launch: function()
    {
        this.removeSplash();
        var jobject = sessionStorage.getItem("jobject");

        if( jobject == null )
        {
            Ext.widget('login');
        }   else
        {
            var jstring = Ext.util.Base64.decode(jobject);
            Apps.Usr.jsession = Ext.decode(jstring);

            if( Apps.Usr.jsession.logged == 'null' )
            {
                Ext.widget('passwd');
            }   else
            {
                Ext.widget('main');
            }
        }
    },

    onAppUpdate: function()
    {
        Ext.Msg.confirm('Application Update', 'This application has an update, reload?', function(choice) { if( choice === 'yes' ) { window.location.reload(); } } );
    }
});
