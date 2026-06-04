Ext.define('Portal.view.header.HeaderBar', {
    extend:'Ext.panel.Panel',
    xtype: 'headerbar',
    margin: 0,
    height:110,
    width: 1200,
    frame: true,
    loadMask: true,
    controller: 'headercontroller',

    requires:['Portal.view.header.HeaderController'],

    layout:
    {
        type: 'hbox',
        align: 'stretch'
    },

    bodyStyle:
    {
        'background-image': 'url(resources/images/backgroundhead.png)',
        'background-size': '100% 110px',
        'background-repeat': 'no-repeat'
    },

    initComponent: function()
    {
        var hbar = this, admin = (Apps.Usr.jsession.profile == 'Admin')?true:false;
        var hname = ' (' + Apps.Usr.jsession.uname + ') ' + Apps.Usr.jsession.fname;

        Ext.applyIf(hbar, {

            items:
            [{
                xtype: 'component',
                margin: '20 20 30 30',
                height: 100,
                width: 200
            },{
                xtype: 'tbspacer', flex: 1
            },{
                xtype: 'container',
                layout: { type: 'vbox', align: 'stretch' },
                width: 600,

                items:
                [{
                    xtype: 'container',
                    layout: { type: 'hbox' , align: 'stretch' },
                    height: 75,

                    items:
                    [{
                        xtype: 'container',
                        layout: { type: 'vbox' , align: 'stretch' },
                        width: 340,

                        items:
                        [{
                            xtype: 'container',
                            height: 30,
                            layout: { type: 'hbox' , align: 'stretch' , pack: 'center' },
                            style:
                            {
                                'background-image': 'url(resources/images/backgroundset.png)',
                                'background-size': '100% 30px',
                                'background-repeat': 'no-repeat'
                            },

                            items:
                            [{
                                xtype: 'splitbutton',
                                text: '<b>Configuración</b>',
                                iconCls: 'x-fa fa-cog',
                                iconAlign: 'left',
                                width: 170,
                                style: { 'border-width': '0px' , 'background-position': 'center center' , 'background-repeat': 'no-repeat' , 'background-color': 'transparent !important' },

                                menu:
                                {
                                    bodyStyle: { 'background': '#E5E9ED' },
                                    width: 175,

                                    items:
                                    [{
                                        xtype:'menuitem',
                                        text: 'Perfil',
                                        hidden: false,
                                        iconCls: 'x-fa fa-user',
                                        listeners: { click: 'onUserDetailsForm' }
                                    },{
                                        xtype:'menuitem',
                                        text: 'Usuarios',
                                        iconAlign: 'left',
                                        iconCls: 'x-fa fa-users',
                                        hidden: !admin,

                                        menu:
                                        {
                                            xtype: 'menu',
                                            bodyStyle: { background: '#E5E9ED' },
                                            floating: true,
                                            width: 120,

                                            items:
                                            [{
                                                xtype:'menuitem',
                                                text: 'Agregar',
                                                hidden: !admin,
                                                iconAlign:'left',
                                                iconCls: 'x-fa fa-user-plus',
                                                listeners: { click: 'onUserAddForm' }
                                            },{
                                                xtype:'menuitem',
                                                text: 'Administrar',
                                                iconAlign:'left',
                                                hidden: !admin,
                                                iconCls: 'x-fa fa-address-book',
                                                listeners: { click: 'onUsersGrid' }
                                            }]
                                        }
                                    }]
                                }
                            },{
                                xtype: 'button',
                                text: '<b>Salir</b>',
                                style: { 'border-width': '0px' , 'background-position': 'center center' , 'background-repeat': 'no-repeat' , 'background-color': 'transparent !important'},
                                listeners: { click: 'onSignOut' },
                                iconCls: 'x-fa fa-power-off',
                                iconAlign: 'rigth',
                                width: 90
                            }]
                        },{
                            xtype: 'container',
                            style: { color: 'rgb(1, 44, 68)' , 'line-height': '2.4' , 'font-size': '12px' , 'font-weight' : 'bold' },
                            layout: { type: 'hbox' , align: 'center' },
                            width: 400,
                            height: 45,

                            items:
                            [{
                                xtype: 'component',
                                itemId: 'notifications',
                                autoEl: { tag: 'img' , src: 'resources/images/user.png' },
                                height: 20,
                                width: 20
                            },{
                                xtype: 'tbspacer', width: 10
                            },{
                                xtype: 'label',
                                itemId:'hname',
                                text: hname,
                                height: 20,
                                width: 380
                            }]
                        }]
                    }]
                }]
            }]
        });

        hbar.callParent(arguments);
    }
});
