Ext.define('Portal.view.users.UsersGrid', {
    extend:'Ext.window.Window',
    alias: 'widget.usersgrid',

    requires:
    [
        'Portal.view.users.UsersController',
        'Ext.ux.ProgressBarPager',
        'Ext.toolbar.Paging',
        'Ext.form.Panel'
    ],

    minHeight:  420,
    minWidth:   900,
    layout:    'fit',
    height:     420,
    width:      800,
    modal:      true,
    autoShow:   true,
    closable:   true,
    constrain:  true,
    closeAction:'hide',
    titleAlign: 'center',
    title:      'Administración de Usuarios',
    controller: 'userscontroller',

    initComponent: function()
    {
        var portal = this;
        var usersStore = Ext.create('Portal.store.Users');

        Ext.applyIf(portal,{

            items:
            [{
                xtype: 'gridpanel',
                itemId:'uGridPanel',
                store: usersStore,

                columns:
                [{
                    xtype: 'rownumberer',
                    width: 35
                },{
                    xtype:'gridcolumn',
                    dataIndex:'usrid',
                    sortable: true,
                    hidden:  true,
                    align:'right',
                    text: 'ID',
                    width: 35
                },{
                    xtype:'gridcolumn',
                    text: 'Nombre',
                    sortable: false,
                    dataIndex:'fname',
                    width: 175
                },{
                    xtype: 'gridcolumn',
                    dataIndex:'profile',
                    text: 'Perfil',
                    sortable: true,
                    width: 75
                },{
                    xtype:'gridcolumn',
                    dataIndex:'uname',
                    text: 'Usuario',
                    sortable: true,
                    width: 75
                },{
                    xtype:'gridcolumn',
                    text: 'Correo',
                    sortable: false,
                    dataIndex:'email',
                    width: 125
                },{
                    xtype:'gridcolumn',
                    text: 'Registrado',
                    dataIndex:'logged',
                    sortable: false,
                    width: 120
                },{
                    xtype:'gridcolumn',
                    text: 'Último Acceso',
                    dataIndex:'login',
                    sortable: false,
                    width: 120
                },{
                    xtype:'gridcolumn',
                    dataIndex:'locked',
                    text: 'Bloqueado',
                    sortable: true,
                    width: 100
                },{
                    xtype: 'actioncolumn',
                    itemId: 'userdelete',
                    menuDisabled: true,
                    sortable: false,
                    width: 25,
                    iconCls: 'x-fa fa-trash-alt',
                    tooltip: 'Eliminar Usuario',
                    handler: 'onUserDeleteForm'
                },{
                    xtype: 'actioncolumn',
                    itemId: 'userupdate',
                    menuDisabled: true,
                    sortable: false,
                    width: 25,
                    iconCls: 'x-fa fa-pen',
                    tooltip: 'Editar Usuario',
                    handler: 'onUserUpdateForm'
                }],

                dockedItems:
                [{
                    xtype: 'pagingtoolbar',
                    store: usersStore,
                    dock: 'bottom',/*
                    listeners: { beforechange: 'action' },*/
                    displayInfo: true,
                    displayMsg: 'Mostrando {0} a {1} de {2} Registros',
                    emptyMsg:   'Sin Resultados Para Mostrar',
                    plugins: { 'ux-progressbarpager' : true }
                }]

            }]

        });

        portal.callParent(arguments);
    }
});
