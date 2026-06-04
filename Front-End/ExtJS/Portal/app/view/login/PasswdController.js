Ext.define('Portal.view.login.PasswdController', {
    extend:'Ext.app.ViewController',
    alias: 'controller.passwd',

    requires: ['Ext.window.MessageBox'],

    onChangePasswd: function()
    {
        var screen = this.getView(); screen.mask("Actualizando");
        var frame = this.getView().down('#passwdForm').getForm();
        var formd = frame.getValues();
        var word = formd.passwordx;
        var vld = true, txt = '';

        if( formd.passwordx != formd.passwordy )
        {
            txt = 'Las contraseñas no coinciden';
            vld = false;
        }

        if( Apps.Usr.jsession.profile == 'Admin' )
        {
            if(!(/[a-z]/.test(word) && /[A-Z]/.test(word) && /[0-9]/.test(word) && /[^A-Za-z0-9]/.test(word)))
            {
                txt = 'La contraseña debe contener:<br>Números<br>Letras Minúsculas<br>Letras Mayúsculas<br>Caracteres Especiales';
                vld = false;
            }

            if( word.length < 20 )
            {
                txt = 'Para un usuario administrador la contraseña debe ser de 20 caracteres';
                vld = false;
            }
        }   else if( word.length < 12 )
        {
            txt = 'La longitud de la contraseña debe ser por lo menos de 12 caracteres';
            vld = false;
        }

        if(!vld)
        {
            screen.unmask();
            Ext.MessageBox.show
            ({
                title: 'Error',
                msg: txt,
                icon: Ext.MessageBox.ERROR,
                buttons: Ext.MessageBox.OK
            }); return;
        }

        var output = { usrid: Apps.Usr.jsession.usrid, paswd: formd.passwordx };

        Ext.Ajax.request
        ({
            url: Apps.Url.root + '/Users/update',
            scope: this,
            encode: false,
            method: 'POST',
            timeout: 60000,
            jsonData: output,
            useDefaultXhrHeader: false,
            headers: { 'Accept': 'application/json', 'Content-Type': 'application/json' },

            success: function(response, options)
            {
                screen.unmask();
                var result = Ext.decode(response.responseText);

                if( result.code == 0 && result.flag == 0 )
                {
                    screen.destroy();
                    Ext.widget('login');
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

                screen.destroy();
                Ext.widget('login');
            }
        });
    }
});
