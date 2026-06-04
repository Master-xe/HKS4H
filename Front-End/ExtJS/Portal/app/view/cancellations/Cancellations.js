Ext.define('Portal.view.cancellations.Cancellations', {
    extend:'Ext.grid.Panel',
    alias: 'widget.cancellations',
    xtype: 'cancellations',

    requires:
    [
        'Portal.view.cancellations.CancellationsController',
        'Portal.store.Documents',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],

    title: 'SAP Documents',
    margin: 20,
    width: 600,
    height: 400,
    frame: true,
    loadMask: true,

    controller: 'cancellations',

    store:
    {
        type: 'documents'
    },

    columns:
    [{
        text: 'UUID',
        dataIndex: 'uuid',
        sortable: false,
        width: 300
    },{
        text: 'Sociedad',
        dataIndex: 'bukrs',
        sortable: true,
        width: 100
    },{
        text: 'Acreedor',
        dataIndex: 'lifnr',
        sortable: true,
        width: 100
    },{
        text: 'Gpo Cuentas',
        dataIndex: 'ktokk',
        sortable: true,
        width: 100
    },{
        text: 'Documento IM',
        dataIndex: 'ibelnr',
        sortable: false,
        width: 100
    },{
        text: 'Clase',
        dataIndex: 'iblart',
        sortable: false,
        width: 70
    },{
        text: 'Estatus',
        dataIndex: 'bstat',
        sortable: false,
        width: 100
    },{
        text: 'Descripción',
        dataIndex: 'isgtxt',
        sortable: false,
        width: 100
    },{
        text: 'Doc Contable',
        dataIndex: 'belnr',
        sortable: false,
        width: 100
    },{
        text: 'Clase',
        dataIndex: 'blart',
        sortable: false,
        width: 70
    },{
        text: 'Fecha Contable',
        dataIndex: 'budat',
        sortable: false,
        width: 100
    },{
        text: 'Referencia',
        dataIndex: 'xblnr',
        sortable: false,
        width: 100
    },{
        text: 'Compensación',
        dataIndex: 'augbl',
        sortable: false,
        width: 100
    },{
        text: 'Vía de Pago',
        dataIndex: 'zlsch',
        sortable: false,
        width: 100
    },{
        xtype: 'checkcolumn',
        header:'Seleccionar',
        dataIndex: 'ichosed',
        headerCheckbox:false,
        width: 100
    }],

    tbar:
    [{
        itemId: 'xsaps',
        xtype: 'button',
        text: 'Aplicar Selección',
        handler: 'onSelectDocument'
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

        if( Apps.Usr.jsession.profile == 'Admin' )
        {
            Ext.ComponentQuery.query("#xsaps")[0].setConfig('hidden', true);
        }
    }
});
