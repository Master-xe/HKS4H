Ext.define('Portal.view.companies.Companies', {
    extend:'Ext.grid.Panel',
    alias: 'widget.companies',
    xtype: 'companies',

    requires:
    [
        'Portal.view.companies.CompaniesController',
        'Portal.store.Companies',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],

    title: 'Sociedades',
    margin: 20,
    width: 600,
    height: 400,
    frame: true,
    loadMask: true,

    controller: 'companies',

    store:
    {
        type: 'companies'
    },

    columns:
    [{
        id: 'crowid',
        text: "No",
        dataIndex: 'cid',
        align: 'right',
        sortable: true,
        hidden: true,
        width: 50
    },{
        xtype: 'actioncolumn',
        itemId: 'deletecmpny',
        menuDisabled: true,
        sortable: false,
        hidden: false,
        width: 25,
        iconCls: 'x-fa fa-trash-alt',
        tooltip: 'Eliminar Sociedad',
        handler: 'onDeleteCompany'
    },{
        xtype: 'actioncolumn',
        itemId: 'editcmpny',
        menuDisabled: true,
        sortable: false,
        hidden: false,
        width: 25,
        iconCls: 'x-fa fa-pen',
        tooltip: 'Editar Sociedad',
        handler: 'onEnrollCompany'
    },{
        text: "Sociedad",
        dataIndex: 'ccode',
        sortable: true,
        width: 200
    },{
        text: "Nombre",
        dataIndex: 'cname',
        sortable: true,
        width: 320
    },{
        text: "RFC",
        dataIndex: 'crfc',
        sortable: false,
        width: 120
    },{
        text: "Registrada",
        dataIndex: 'cdate',
        sortable: false,
        width: 175
    },{
        text: "Actualizada",
        dataIndex: 'cupdt',
        sortable: false,
        width: 175
    },{
        text: "Bloqueada",
        dataIndex: 'clock',
        sortable: true,
        width: 100
    }],

    tbar:
    [{
        xtype: 'textfield',
        itemId:'scmpnyrfc',
        hideLabel: true,
        emptyText: 'RFC Receptor',
        fieldLabel:'RFC Receptor',
        width: 200
    },{
        xtype: 'combobox',
        name: 'sttscmpny',
        itemId:'cmpnyst',
        editable: false,
        hideLabel: true,
        value: '',
        width: 120,
        store: [["","Todas"],["Y","Bloqueadas"],["N","Desbloqueadas"]]
    },{
        xtype: 'button',
        tooltip: 'Buscar Sociedad',
        handler: 'onSearchCompany',
        iconCls: 'x-fa fa-search'
    },{
        xtype: 'button',
        itemId: 'cmpnyenroll',
        tooltip: 'Registrar Sociedad',
        handler: 'onEnrollCompany',
        iconCls: 'x-fa fa-upload'
    }],

    bbar:
    {
        xtype: 'pagingtoolbar',
        displayInfo: true,
        displayMsg: 'Mostrando {0} a {1} de {2} Registros',
        emptyMsg:   'Sin Resultados Para Mostrar',
        plugins: { 'ux-progressbarpager' : true }
    },

    style: 'border: solid gray 1px',

    initComponent: function()
    {
        this.callParent(arguments);
        var admin = (Apps.Usr.jsession.profile == 'Admin') ? true : false;

        if(!admin)
        {
            Ext.ComponentQuery.query("#editcmpny")[0].destroy();
            Ext.ComponentQuery.query("#deletecmpny")[0].destroy();
            Ext.ComponentQuery.query("#cmpnyenroll")[0].setConfig('hidden', true);
        }
    }
});
