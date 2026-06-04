Ext.define('Portal.view.login.LoginController', {
    extend:'Ext.app.ViewController',
    alias: 'controller.login',

    requires: ['Ext.window.MessageBox'],

    onSignIn: function()
    {
        var screen = this.getView(); screen.mask("Verificando");
        var frame = this.getView().down('#loginForm').getForm();
        var formd = frame.getValues(), jstring = '';
        var output = Ext.util.Base64.encode(formd.username + '/' + formd.password);/*
        var udata = { usrid: 2, profile: 'Admin', uname: 'System', fname: 'Administrator', email: 'example@domain.com', locked: 'Y' };
        output = JSON.stringify(udata);
        jstring = Ext.util.Base64.encode(output);
        sessionStorage.setItem('jobject', jstring);
        Apps.Usr.jsession = udata;
        screen.destroy();
        Ext.widget('passwd');*/
        Ext.Ajax.request
        ({
            url: Apps.Url.root + '/Users/signin/' + output,
            scope: this,
            encode: false,
            method: 'POST',
            timeout: 60000,
            useDefaultXhrHeader: false,

            success: function(response, options)
            {
                screen.unmask();
                var result = Ext.decode(response.responseText);

                if( result.code == 0 && result.flag == 0 )
                {
                    Apps.Usr.jsession = Ext.decode(result.text);
                    jstring = Ext.util.Base64.encode(result.text);
                    sessionStorage.setItem('jobject', jstring);

                    if( Apps.Usr.jsession.logged == 'null' )
                    {
                        screen.destroy();
                        Ext.widget('passwd');
                    }   else
                    {
                        screen.destroy();
                        Ext.widget('main');
                    }
                }   else
                {
                    Ext.MessageBox.show
                    ({
                        title: 'Error',
                        msg: result.text,
                        icon: Ext.MessageBox.ERROR,
                        buttons: Ext.MessageBox.OK
                    });
                }
            }, failure: function(response, options)
            {
                frame.reset();
                screen.unmask();

                Ext.MessageBox.show
                ({
                    title: 'Error: ' + response.status,
                    msg: response.responseText,
                    icon: Ext.MessageBox.ERROR,
                    buttons: Ext.MessageBox.OK
                });
            }
        });
    }
});
