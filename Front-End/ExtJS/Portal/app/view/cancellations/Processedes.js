Ext.define('Portal.view.cancellations.Processedes', {
    extend:'Ext.grid.Panel',
    alias: 'widget.processedes',
    xtype: 'processedes',

    requires:
    [
        'Portal.view.cancellations.CancellationsController',
        'Portal.store.Cancellations',
        'Portal.store.Catalogs',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],

    title: 'Procesadas',
    margin: 20,
    width: 600,
    height: 400,
    frame: true,
    loadMask: true,

    controller: 'cancellations',

    store:
    {
        type: 'cancellations'
    },

    columns:
    [{
        text: 'ID',
        dataIndex: 'docid',
        sortable: true,
        hidden: true,
        width: 50
    },{
        text: 'UUID',
        dataIndex: 'uuid',
        sortable: true,
        lockable: true,
        locked: true,
        width: 300
    },{
        text: 'Tipo',
        dataIndex: 'doctype',
        sortable: false,
        hidden: true,
        width: 50
    },{
        text: 'Fecha Emisión',
        dataIndex: 'idate',
        sortable: false,
        width: 120
    },{
        text: 'Emisor',
        dataIndex: 'rfce',
        sortable: true,
        width: 100
    },{
        text: 'Receptor',
        dataIndex: 'rfcr',
        sortable: true,
        width: 100
    },{
        text: 'Monto',
        dataIndex: 'amount',
        sortable: false,
        width: 100
    },{
        text: 'Moneda',
        dataIndex: 'currency',
        sortable: false,
        width: 100
    },{
        text: 'Proceso',
        dataIndex: 'pstype',
        sortable: true,
        width: 100
    },{
        text: 'Resultado',
        dataIndex: 'kresult',
        sortable: true,
        width: 100
    },{
        text: 'Estatus',
        dataIndex: 'kstatus',
        sortable: true,
        width: 100
    },{
        text: 'Mensaje',
        dataIndex: 'kmessage',
        sortable: false,
        width: 100
    },{
        text: 'Búsqueda',
        dataIndex: 'sstat',
        sortable: false,
        hidden: true,
        width: 100
    },{
        text: 'Estatus',
        dataIndex: 'bstat',
        sortable: false,
        hidden: true,
        width: 100
    },{
        text: 'Descripción',
        dataIndex: 'isgtxt',
        sortable: false,
        width: 100
    },{
        text: 'Compensación',
        dataIndex: 'augbl',
        sortable: false,
        width: 100
    },{
        text: 'Registrado',
        dataIndex: 'date',
        sortable: true,
        width: 125
    },{
        text: 'Actualizado',
        dataIndex: 'updt',
        sortable: false,
        width: 125
    },{
        text: 'Respuesta',
        dataIndex: 'response',
        sortable: false,
        width: 100
    },{
        xtype: 'actioncolumn',
        itemId:'xmlreceipt',
        menuDisabled: true,
        sortable: false,
        hidden: false,
        width: 25,
        iconCls: 'x-fa fa-file-invoice',
        tooltip: 'Visualizar Acuse',
        handler: 'onViewReceipt'
    },{
        xtype: 'actioncolumn',
        itemId:'xmldocument',
        menuDisabled: true,
        sortable: false,
        hidden: false,
        width: 25,
        iconCls: 'x-fa fa-file-code',
        tooltip: 'Ver XML',
        handler: 'onViewXML'
    },{
        xtype: 'actioncolumn',
        itemId:'verifycfdi',
        menuDisabled: true,
        sortable: false,
        hidden: false,
        width: 25,
        iconCls: 'x-fa fa-link',
        tooltip: 'Verifica CFDI',
        handler: 'onViewSAT'
    },{
        text: 'Usuario',
        dataIndex: 'user',
        sortable: true,
        width: 100
    }],

    tbar:
    [{
        xtype: 'textfield',
        itemId:'ycfdiuuid',
        hideLabel: true,
        emptyText: 'Folio Fiscal(uuid)',
        fieldLabel:'Folio Fiscal(uuid)',
        width: 200
    },{
        xtype: 'combobox',
        name:  'companid',
        itemId:'ycmpnyid',
        publishes:'label',
        queryMode:'remote',
        valueField:'entry',
        displayField:'label',
        allowBlank: true,
        hideLabel: true,
        editable: false,
        width: 200,
        store: { type: 'catalogs' }
    },{
        xtype: 'datefield',
        itemId:'ystrtdate',
        fieldLabel:'De',
        allowBlank: true,
        value: new Date(),
        maxValue: new Date(),
        labelWidth: 25,
        width: 160
    },{
        xtype: 'datefield',
        itemId:'yenddate',
        fieldLabel: 'a',
        allowBlank: true,
        value: new Date(),
        maxValue: new Date(),
        labelWidth: 25,
        width: 160
    },{
        xtype: 'button',
        tooltip: 'Filtrar',
        handler: 'onSearchProcessedes',
        iconCls: 'x-fa fa-search'
    },{
        xtype:'button',
        itemId: 'yexportxlsx',
        tooltip: 'Generar Excel',
        handler: 'onExportExcel',
        iconCls: 'x-fa fa-file-excel'
    }],

    bbar:
    {
        xtype: 'pagingtoolbar',
        displayInfo: true,
        displayMsg: 'Mostrando {0} a {1} de {2} Registros',
        emptyMsg:   'Sin Resultados Para Mostrar',
        plugins: { 'ux-progressbarpager' : true }
    },  style: 'border: solid gray 1px'
});
