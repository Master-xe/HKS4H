Ext.define('Portal.view.users.UsersController', {
    extend:'Ext.app.ViewController',
    alias: 'controller.userscontroller',

    onUserDeleteForm: function(grid, row, column)
    {
        var record = grid.getStore().getAt(row), vw = this;
        var extmsg = 'Esta seguro de eliminar el usuario <b>' + record.get('uname') + '</b>?';

        Ext.MessageBox.show
        ({
            title: 'Confirmación!',
            msg: extmsg,
            icon: Ext.MessageBox.QUESTION,
            buttons: Ext.MessageBox.YESNO,
            fn: function(choice)
            {
                if( choice !== 'yes' ){ return; }
                vw.onUserDelete(record.get('usrid'));
            }
        });
    },

    onUserUpdateForm: function(grid, row, column)
    {
        var udetails = Ext.create('Portal.view.users.UserDetails');
        var userform = udetails.down('#userDetailsForm').getForm().reset();
        var userdata = grid.getStore().getAt(row).getData(true);
        userform.setValues(userdata);
        udetails.show();
    },

    onCancelUserEnroll: function(source)
    {
        var vw = this.getView(); vw.down('#userAddForm').getForm().reset(); vw.hide();
    },

    onCancelUserUpdate: function(source)
    {
        this.getView().hide();
    },

    onUserDelete: function(uident)
    {
        var vw = this.getView();
        vw.mask('Eliminando...');

        Ext.Ajax.request
        ({
            url: Apps.Url.root + '/Users/delete/' + uident,
            scope: this,
            encode: false,
            method: 'POST',
            timeout: 60000,
            useDefaultXhrHeader: false,

            success: function(response, options)
            {
                var iconType = null, headerTitle; vw.unmask();
                var result = Ext.decode(response.responseText);

                headerTitle = (result.code == 0) ? 'Ejecución Exitosa' : result.type + ': ' + result.code;

                switch(result.flag)
                {
                    case -1: iconType = Ext.MessageBox.ERROR; break;
                    case  1: iconType = Ext.MessageBox.INFO;  break;
                    case  2: iconType = Ext.MessageBox.QUESTION; break;
                    case  3: iconType = Ext.MessageBox.WARNING;  break;
                    default: iconType = Ext.MessageBox.INFO;
                }

                Ext.MessageBox.show
                ({
                    title: headerTitle,
                    msg: result.text,
                    icon: iconType,
                    buttons: Ext.MessageBox.OK
                });
            },  failure: function(response, options)
            {
                vw.unmask();
                Ext.MessageBox.show
                ({
                    title: 'Error: ' + response.status,
                    msg: response.responseText,
                    icon: Ext.MessageBox.ERROR,
                    buttons: Ext.MessageBox.OK
                });
            }
        });
    },

    onUserEnroll: function(source)
    {
        var v = this.getView(), etitle = 'ERROR', emessage = 'Por favor verifique la información';
        var uform = v.down('#userAddForm').getForm(), udata = uform.getValues();

        if(!uform.isValid())
        {
            Ext.MessageBox.show
            ({
                title: etitle,
                msg: emessage,
                icon: Ext.MessageBox.ERROR,
                buttons: Ext.MessageBox.OK
            }); return;
        }

        this.onServiceRequest('Procesando...', '/Users/signup', udata, uform);
    },

    onUserUpdate: function(source)
    {
        var v = this.getView(), etitle = 'ERROR', emessage = 'Por favor verifique la información';
        var uform = v.down('#userDetailsForm').getForm(), udata = uform.getValues(), validForm = true;

        if(!uform.isValid())
        {
            validForm = false;
        }

        if( udata.paswd.length > 0 )
        {
            if( udata.paswd != udata.usrpaswd2 )
            {
                emessage = 'Las contraseñas no coinciden!';
                validForm = false;
            }

            if( udata.profile == 'Admin' )
            {
                if(!(/[a-z]/.test(udata.paswd) && /[A-Z]/.test(udata.paswd) && /[0-9]/.test(udata.paswd) && /[^A-Za-z0-9]/.test(udata.paswd)))
                {
                    emessage = 'La contraseña debe contener:<br>Números<br>Letras Minúsculas<br>Letras Mayúsculas<br>Caracteres Especiales';
                    validForm = false;
                }

                if( udata.paswd.length < 20 )
                {
                    emessage = 'Para un usuario administrador la contraseña debe ser de 20 caracteres';
                    validForm = false;
                }
            }   else if( udata.paswd.length < 12 )
            {
                emessage = 'La longitud de la contraseña debe ser por lo menos de 12 caracteres';
                validForm = false;
            }
        }

        if(!validForm)
        {
            Ext.MessageBox.show
            ({
                title: etitle,
                msg: emessage,
                icon: Ext.MessageBox.ERROR,
                buttons: Ext.MessageBox.OK
            }); return;
        }

        this.onServiceRequest('Actualizando...', '/Users/update', udata, uform);
    },

    onServiceRequest: function(maskmsg, endpoint, jdata, xform)
    {
        var v = this.getView();
        v.mask(maskmsg);

        Ext.Ajax.request
        ({
            url: Apps.Url.root + endpoint,
            scope: this,
            encode: true,
            method: 'POST',
            timeout: 60000,
            jsonData: jdata,
            useDefaultXhrHeader: false,
            headers: { 'Accept': 'application/json', 'Content-Type': 'application/json' },

            success: function(response, options)
            {
                var iconType = null, headerTitle; v.unmask();
                var result = Ext.decode(response.responseText);

                if( result.code == 0 && endpoint == '/Users/signup' )
                {
                    xform.reset();
                }

                switch(result.flag)
                {
                    case -1: iconType = Ext.MessageBox.ERROR; break;
                    case  1: iconType = Ext.MessageBox.INFO;  break;
                    case  2: iconType = Ext.MessageBox.QUESTION; break;
                    case  3: iconType = Ext.MessageBox.WARNING;  break;
                    default: iconType = Ext.MessageBox.INFO;
                }

                headerTitle = (result.code == 0 && result.flag == 0) ? 'Ejecución Exitosa' : result.type + ': ' + result.code;

                Ext.MessageBox.show
                ({
                    title: headerTitle,
                    msg: result.text,
                    icon: iconType,
                    buttons: Ext.MessageBox.OK
                });
            }, failure: function(response, options)
            {
                v.unmask();
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
