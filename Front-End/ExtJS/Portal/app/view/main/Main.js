Ext.define('Portal.view.main.Main', {
    extend:'Ext.container.Container',

    requires:
    [
        'Portal.view.main.MainController',
        'Portal.view.main.MainModel'
    ],

    xtype:      'main',
    controller: 'main',
    plugins:    'viewport',

    viewModel:
    {
        type:   'mainmodel'
    },

    layout:
    {
        type:   'border'
    },

    items:
    [{
        xtype:  'headerbar',
        region: 'north'
    },{
        id: 'MainCenterTabPanel',
        region: 'center',
        xtype:  'tabpanel',
        items:
        [{
            title:  'Home',
            xtype:  'home',
            iconCls:'x-fa fa-home'
        },{
            title:  'Sociedades',
            xtype:  'companies',
            iconCls:'x-fa fa-city'
        },{
            title:  'Procesados',
            xtype:  'processedes',
            iconCls:'x-fa fa-list-ul'
        },{
            title:  'Pendientes',
            xtype:  'pendings',
            iconCls:'x-fa fa-question'
        },{
            title:  'SAP Documents',
            xtype:  'cancellations',
            iconCls:'x-fa fa-list-ol'
        }]
    }]
});
