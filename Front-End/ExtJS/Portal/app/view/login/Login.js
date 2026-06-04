Ext.define('Portal.view.login.Login', {
    extend:'Ext.window.Window',
    xtype: 'login',

    requires:
    [
        'Portal.view.login.LoginController',
        'Ext.form.Panel'
    ],

    layout:
    {
        type:   'vbox',
        align:  'stretch'
    },

    height:     420,
    width:      320,
    modal:      false,
    closable:   false,
    constrain:  true,
    controller: 'login',
    autoShow:   true,
    title:      'Portal de Administración',

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
                padding:    '10,10,10,10',
                flex: 1.5,
                autoEl:
                {
                    tag:'img',
                    src:'resources/images/login.png'
                }
            },{
                xtype:'form',
                itemId:'loginForm',
                bodyPadding:10,
                border:false,
                bodyStyle:{'background':'transparent'},
                layout:{type:'vbox',align:'stretch',pack:'center'},

                items:
                [{
                    xtype:'textfield',
                    vtype:'alphanum',
                    anchor:'100%',
                    maxLength:16,
                    minLength:2,
                    labelAlign:'top',
                    allowBlank:false,
                    name:'username',
                    fieldLabel:'Usuario',
                    allowOnlyWhitespace:false
                },{
                    xtype:'textfield',
                    anchor:'100%',
                    maxLength:20,
                    minLength:12,
                    labelAlign:'top',
                    allowBlank:false,
                    name:'password',
                    inputType: 'password',
                    fieldLabel:'Contraseña',
                    allowOnlyWhitespace:false
                }],

                buttons:
                [{
                    text:'Entrar',
                    formBind:true,
                    itemId:'bsignin',
                    listeners:{click:'onSignIn'}
                }]
            }]
        });

        portal.callParent(arguments);
    }
});
