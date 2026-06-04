Ext.define('Portal.view.users.UserAdd', {
    extend:'Ext.window.Window',
    alias: 'widget.useradd',
    title: 'Alta de Usuario',
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
        labelWidth: 90,
        msgTarget: 'side',
        labelStyle: 'font-weight:bold'
    },

    items:
    [{
        xtype: 'form',
        itemId: 'userAddForm',

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
                    fieldLabel:'Perfil de Usuario',
                    xtype:'combobox',
                    itemId:'usrtype',
                    name:  'rolid',
                    margin:'0 0 2 0',
                    labelWidth:120,
                    width: 350,
                    value: 3,
                    store: [['2','Admin'],['3','Fiscal']]
                },{
                    fieldLabel:'Nombre Usuario',
                    itemId:'usruname',
                    vtype: 'alphanum',
                    name: 'uname',
                    margin:'0 0 2 0',
                    allowBlank:false,
                    labelWidth: 120,
                    maxLength: 16,
                    minLength: 4,
                    width: 350
                },{
                    fieldLabel:'Nombre Completo',
                    itemId:'usrfname',
                    name: 'fname',
                    margin:'0 0 2 0',
                    allowBlank:false,
                    labelWidth:120,
                    maxLength: 64,
                    width: 350
                },{
                    fieldLabel:'Correo Electrónico',
                    itemId:'usremail',
                    name: 'email',
                    margin:'0 0 2 0',
                    allowBlank:false,
                    labelWidth:120,
                    vtype:'email',
                    width: 350
                }]
            }],
        }],
    }],

    buttons:
    [{
        text: 'Cancelar',
        iconCls: 'x-fa fa-ban',
        handler: 'onCancelUserEnroll',
        iconAlign: 'right'
    },{
        text: 'Registrar',
        iconCls: 'x-fa fa-check',
        handler: 'onUserEnroll',
        iconAlign: 'right'
    }]
});
