Ext.define('Portal.view.login.Passwd', {
    extend:'Ext.window.Window',
    xtype: 'passwd',

    requires:
    [
        'Portal.view.login.PasswdController',
        'Ext.form.Panel'
    ],

    layout:
    {
        type:   'vbox',
        align:  'stretch'
    },

    height:     350,
    width:      300,
    modal:      false,
    closable:   false,
    constrain:  true,
    controller: 'passwd',
    autoShow:   true,
    title:      'Actualización de Contraseña',

    bodyStyle:
    {
        'background-image'  : 'url(resources/images/backgroundlogin.jpg)',
        'background-size'   : '100% 100%',
        'background-repeat' : 'no-repeat'
    },

    initComponent: function()
    {
        var portal = this;

        Ext.applyIf(portal, {

            items:
            [{
                xtype:      'component',
                padding:    '10,10,10,10'
            },{
                xtype:'form',
                itemId:'passwdForm',
                bodyPadding:10,
                border:false,
                bodyStyle:{'background':'transparent'},
                layout:{type:'vbox',align:'stretch',pack:'center'},

                items:
                [{
                    xtype:'container',
                    html: 'Se ha detectado el acceso por primera vez.<br>Por favor cambie su contraseña',
                    style: { color: 'rgb(1, 44, 68)' , 'line-height': '2.4' , 'font-size': '11px' , 'font-weight' : 'bold' }
                },{
                    xtype:'textfield',
                    anchor:'100%',
                    maxLength:20,
                    minLength:12,
                    labelAlign:'top',
                    allowBlank:false,
                    name:'passwordx',
                    inputType: 'password',
                    fieldLabel:'Contraseña',
                    allowOnlyWhitespace:false
                },{
                    xtype:'textfield',
                    anchor:'100%',
                    maxLength:20,
                    minLength:12,
                    labelAlign:'top',
                    allowBlank:false,
                    name:'passwordy',
                    inputType: 'password',
                    fieldLabel:'Repetir Contraseña',
                    allowOnlyWhitespace:false
                }],

                buttons:
                [{
                    text:'Actualizar',
                    formBind:true,
                    itemId:'passwdbtn',
                    listeners:{click:'onChangePasswd'}
                }]
            }]
        });

        portal.callParent(arguments);
    }
});
