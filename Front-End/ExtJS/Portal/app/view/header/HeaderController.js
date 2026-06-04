Ext.define('Portal.view.header.HeaderController', {
    extend:'Ext.app.ViewController',
    alias: 'controller.headercontroller',

    requires: ['Ext.window.MessageBox'],

    onUserDetailsForm: function(source)
    {
        var udetails = Ext.create('Portal.view.users.UserDetails');
        var userform = udetails.down('#userDetailsForm').getForm().reset();
        userform.setValues(Apps.Usr.jsession);
        udetails.show();
    },

    onUserAddForm: function(source)
    {
        var useradd = Ext.create('Portal.view.users.UserAdd').show();
    },

    onUsersGrid: function(source)
    {
        var users = Ext.create('Portal.view.users.UsersGrid').show();
    },

    onSignOut: function(source)
    {
        var me = this;
        var vw = this.getView();

        Ext.MessageBox.show
        ({
            title: 'Confirmación!',
            msg: 'Esta seguro de cerrar la sesión?',
            icon: Ext.MessageBox.QUESTION,
            buttons: Ext.MessageBox.YESNO,
            fn: function(choice)
            {
                if( choice !== 'yes' ){ return; }

                vw.mask('Cerrando Session...');

                Ext.Ajax.request
                ({
                    url: Apps.Url.root + '/Users/logout',
                    scope: this,
                    encode: false,
                    method: 'POST',
                    timeout: 60000,
                    useDefaultXhrHeader: false,

                    success: function(response, options)
                    {
                        vw.unmask();
                        Ext.getCmp("MainCenterTabPanel").destroy();
                        sessionStorage.removeItem('jobject');
                        Apps.Usr.jsession = null;
                        me.getView().destroy();
                        Ext.widget('login');

                    },  failure: function(response, options)
                    {   console.log(response);
                        Ext.getCmp("MainCenterTabPanel").destroy();
                        sessionStorage.removeItem('jobject');
                        Apps.Usr.jsession = null;
                        me.getView().destroy();
                        Ext.widget('login');
                    }
                });
            }
        });
    }
});
