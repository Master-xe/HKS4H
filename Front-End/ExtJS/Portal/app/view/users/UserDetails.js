Ext.define('Portal.view.users.UserDetails', {
    extend:'Ext.window.Window',
    alias: 'widget.userdetails',
    title: 'Información de Usuario',

    modal: true,
    width: 420,
    minWidth: 420,
    minHeight: 320,
    resizable: true,
    bodyPadding: 10,
    closeAction: 'hide',
    controller: 'userscontroller',

    fieldDefaults:
    {
        labelAlign: 'right',
        labelWidth: 100,
        msgTarget: 'side',
        labelStyle: 'font-weight:bold'
    },

    initComponent: function()
    {
        var udtls = this, admin = (Apps.Usr.jsession.profile == 'Admin') ? true : false;

        Ext.applyIf(udtls,{
            items:
            [{
                xtype: 'form',
                itemId: 'userDetailsForm',

                items:
                [{
                    xtype: 'fieldset',
                    layout:'anchor',
                    title: 'Datos de Usuario',
                    defaultType: 'textfield',
                    collapsible:true,
                    defaults: { anchor: '100%' , componentCls: "" },

                    items:
                    [{
                        xtype: 'container',
                        layout: 'vbox',
                        margin: '0 0 0 0',
                        defaultType: 'textfield',

                        items:
                        [{
                            xtype: 'hiddenfield',
                            itemId:'usrid',
                            value: null,
                            name:'usrid'
                        },{
                            fieldLabel:'Perfil de Usuario',
                            itemId:'profile',
                            name:  'profile',
                            margin:'0 0 2 0',
                            editable: false,
                            labelWidth: 120,
                            width: 350
                        },{
                            fieldLabel:'Nombre Usuario',
                            itemId:'uname',
                            name:  'uname',
                            margin:'0 0 2 0',
                            editable: false,
                            labelWidth: 120,
                            width: 350
                        },{
                            fieldLabel:'Nombre Completo',
                            itemId:'fname',
                            name:  'fname',
                            margin:'0 0 2 0',
                            editable: false,
                            labelWidth: 120,
                            width: 350
                        },{
                            fieldLabel:'Correo Electrónico',
                            itemId:'email',
                            name:  'email',
                            margin:'0 0 2 0',
                            editable: true,
                            allowBlank:false,
                            editable: true,
                            labelWidth:120,
                            vtype:'email',
                            width: 350
                        },{
                            emptyText: 'Cambiar Contraseña',
                            fieldLabel:'Contraseña',
                            inputType: 'password',
                            itemId:'paswd',
                            name:  'paswd',
                            margin:'0 0 2 0',
                            labelWidth:120,
                            minLength: 12,
                            maxLength: 20,
                            width: 350
                        },{
                            emptyText: 'Repetir Contraseña',
                            fieldLabel:'Contraseña',
                            inputType: 'password',
                            itemId:'usrpaswd2',
                            name:  'usrpaswd2',
                            margin:'0 0 2 0',
                            labelWidth:120,
                            minLength: 12,
                            maxLength: 20,
                            width: 350
                        },{
                            fieldLabel:'Estatus',
                            xtype:'combobox',
                            itemId:'locked',
                            name:  'locked',
                            margin:'0 0 2 0',
                            editable: true,
                            labelWidth:120,
                            hidden:!admin,
                            value: 'N',
                            width: 350,
                            store: [['Y','Bloqueado'],['N','Desbloqueado']]
                        }]
                    }],
                }],

                buttons:
                [{
                    text: 'Cancelar',
                    iconCls: 'x-fa fa-ban',
                    handler: 'onCancelUserUpdate',
                    iconAlign: 'right'
                },{
                    text: 'Actualizar',
                    iconCls: 'x-fa fa-check',
                    handler: 'onUserUpdate',
                    iconAlign: 'right'
                }]
            }]
        });

        udtls.callParent(arguments);
    }
});
