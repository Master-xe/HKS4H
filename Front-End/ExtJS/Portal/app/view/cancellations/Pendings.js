Ext.define('Portal.view.cancellations.Pendings', {
    extend:'Ext.grid.Panel',
    alias: 'widget.pendings',
    xtype: 'pendings',

    requires:
    [
        'Portal.view.cancellations.CancellationsController',
        'Portal.store.Cancellations',
        'Portal.store.Catalogs',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],

    title: 'Pendientes',
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
        hidden:  true,
        width: 100
    },{
        text: 'Datos CFDI',
        columns:
        [{
            text: 'UUID',
            dataIndex: 'uuid',
            sortable: true,
            width: 300
        },{
            text: 'Tipo',
            dataIndex: 'doctype',
            sortable: false,
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
            text: 'Serie',
            dataIndex: 'serie',
            sortable: false,
            width: 100
        },{
            text: 'Folio',
            dataIndex: 'folio',
            sortable: false,
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
        }]
    },{
        text: 'Etapa',
        dataIndex: 'stage',
        sortable: false,
        width: 70
    },{
        text: 'Konesh',
        columns:
        [{
            text: 'Proceso',
            dataIndex: 'pstype',
            sortable: true,
            width: 100
        },{
            text: 'Pedido',
            dataIndex: 'porder',
            sortable: false,
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
        }]
    },{
        text: 'SAP ECC IM/FI',
        columns:
        [{
            text: 'Búsqueda',
            dataIndex: 'sstat',
            sortable: false,
            width: 100
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
            sortable: false,
            width: 100
        },{
            text: 'Doc IM',
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
            text: 'Estatus',
            dataIndex: 'response',
            sortable: false,
            width: 100
        },{
            text: 'Horas',
            dataIndex: 'elapsed',
            sortable: true,
            width: 70
        },{
            xtype: 'actioncolumn',
            itemId:'xxmldocument',
            menuDisabled: true,
            sortable: false,
            hidden: false,
            width: 25,
            iconCls: 'x-fa fa-file-code',
            tooltip: 'Ver XML',
            handler: 'onViewXML'
    },{
            xtype: 'actioncolumn',
            itemId:'xverifycfdi',
            menuDisabled: true,
            sortable: false,
            hidden: false,
            width: 25,
            iconCls: 'x-fa fa-link',
            tooltip: 'Verifica CFDI',
            handler: 'onViewSAT'
        }]
    },{
        width: 120,
        itemId:'xrespond',
        text: 'Responder',
        xtype: 'widgetcolumn',
        widget:
        {
            allowBlank:true,
            editable: false,
            xtype: 'combobox',
            store: ['Pendiente','Aceptacion','Rechazo'],
            bind: '{record.answer}'
        }
    }],

    tbar:
    [{
        xtype: 'textfield',
        itemId:'ncfdiuuid',
        hideLabel: true,
        emptyText: 'Folio Fiscal(uuid)',
        fieldLabel:'Folio Fiscal(uuid)',
        width: 200
    },{
        xtype: 'combobox',
        name:  'companid',
        itemId:'ncmpnyid',
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
        itemId:'nstrtdate',
        fieldLabel:'De',
        allowBlank: true,
        value: new Date(),
        maxValue: new Date(),
        labelWidth: 25,
        width: 160
    },{
        xtype: 'datefield',
        itemId:'nenddate',
        fieldLabel: 'a',
        allowBlank: true,
        value: new Date(),
        maxValue: new Date(),
        labelWidth: 25,
        width: 160
    },{
        xtype: 'button',
        tooltip: 'Filtrar',
        handler: 'onSearchPendings',
        iconCls: 'x-fa fa-search'
    },{
        xtype:'button',
        itemId: 'xexportxlsx',
        tooltip: 'Generar Excel',
        handler: 'onExportExcel',
        iconCls: 'x-fa fa-file-excel'
    },{
        xtype: 'button',
        text:  'Responder',
        itemId:'btrespond',
        handler: 'onRespond'
    }],

    bbar:
    {
        xtype: 'pagingtoolbar',
        displayInfo: true,
        displayMsg: 'Mostrando {0} a {1} de {2} Registros',
        emptyMsg:   'Sin Resultados Para Mostrar',
        plugins: { 'ux-progressbarpager' : true }
    },  style: 'border: solid gray 1px',

    initComponent: function()
    {
        this.callParent(arguments);
        var admin = (Apps.Usr.jsession.profile == 'Admin') ? true : false;

        if( admin )
        {
            Ext.ComponentQuery.query("#xrespond")[0].destroy();
            Ext.ComponentQuery.query("#btrespond")[0].setConfig('hidden', true);
        }
    }
});
