Ext.define('Portal.view.companies.CompanyForm', {
    extend:'Ext.window.Window',
    xtype: 'company-form-window',
    reference: 'cformwindow',

    title: 'Registrar Sociedad',
    modal: true,
    width: 520,
    minWidth: 520,
    minHeight: 320,
    resizable: true,
    bodyPadding: 10,
    closeAction: 'hide',

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
        reference: 'companyform',

        items:
        [{
            xtype: 'fieldset',
            title: 'Datos de la Empresa',
            defaultType: 'textfield',
            layout: 'anchor',
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
                    xtype: 'hiddenfield',
                    itemId: 'companyid',
                    name:  'companyid',
                    value: null
                },{
                    emptyText: 'Nombre de la Empresa o Razón Social',
                    fieldLabel: 'Entidad',
                    itemId: 'icmpnyname',
                    name: 'icmpnyname',
                    allowBlank: false,
                    margin: '0 0 2 0',
                    labelWidth: 80,
                    maxLength:  64,
                    minLength:  2,
                    width: 350
                },{
                    emptyText: 'Registro Federal de Contribuyentes',
                    fieldLabel: 'R.F.C.',
                    itemId: 'icmpnyrfc',
                    name: 'icmpnyrfc',
                    vtype:'alphanum',
                    allowBlank: false,
                    margin: '0 0 2 0',
                    labelWidth: 80,
                    maxLength: 13,
                    minLength: 12,
                    width: 350
                },{
                    emptyText: 'Código de Sociedad',
                    fieldLabel: 'Sociedad',
                    itemId: 'icmpnycode',
                    name: 'icmpnycode',
                    allowBlank: false,
                    margin: '0 0 2 0',
                    labelWidth: 80,
                    maxLength:  4,
                    minLength:  1,
                    width: 350
                },{
                    emptyText: 'Contraseña CSD y FIEL',
                    fieldLabel: 'Contraseña',
                    inputType: 'password',
                    itemId: 'icpassword',
                    name: 'icpassword',
                    allowBlank: false,
                    margin: '0 0 2 0',
                    labelWidth: 80,
                    maxLength: 20,
                    minLength: 10,
                    width: 350
                },{
                    emptyText: 'Repetir Contraseña',
                    fieldLabel: 'Contraseña',
                    inputType: 'password',
                    itemId: 'ecpassword',
                    name: 'ecpassword',
                    allowBlank: false,
                    margin: '0 0 2 0',
                    labelWidth: 80,
                    maxLength: 20,
                    minLength: 10,
                    width: 350
                },{
                    xtype: 'combobox',
                    name: 'icmpnystat',
                    itemId:'icmpnystat',
                    fieldLabel:'Estatus',
                    margin: '0 0 2 0',
                    editable: false,
                    labelWidth: 80,
                    value: 'N',
                    width: 350,
                    store: [['Y','Bloqueada'],['N','Desbloqueada']]
                },{
                    xtype: 'combobox',
                    name: 'idtreqtype',
                    itemId:'idtreqtype',
                    fieldLabel:'Solicitud',
                    margin: '0 0 2 0',
                    editable: false,
                    labelWidth: 80,
                    hidden: true,
                    value:'CFDI',
                    width: 350,
                    store: [['CFDI','CFDI'],['Metadata','Metadata']]
                },{
                    emptyText: 'Certificado Sello Digital',
                    fieldLabel: 'CSD',
                    buttonText: 'Buscar',
                    itemId: 'icsdfile',
                    xtype: 'filefield',
                    name: 'icsdfile',
                    allowBlank:false,
                    labelWidth: 80,
                    width: 420,
                    listeners: { change: 'onLoadCSDBase64', afterrender: 'onFileFilter' }
                },{
                    emptyText: 'Firma Electrónica Avanzada',
                    fieldLabel: 'FIEL',
                    buttonText: 'Buscar',
                    itemId: 'ipfxfile',
                    xtype: 'filefield',
                    name: 'ipfxfile',
                    allowBlank:false,
                    labelWidth: 80,
                    width: 420,
                    listeners: { change: 'onLoadPFXBase64', afterrender: 'onFileFilter' }
                }]
            }],
        }],
    }],

    buttons:
    [{
        text: 'Cancelar',
        iconCls: 'x-fa fa-ban',
        handler: 'onCancelEnroll',
        iconAlign: 'right'
    },{
        text: 'Registrar',
        itemId: 'mgrcmpny',
        iconCls: 'x-fa fa-check',
        handler: 'onFormSubmit',
        iconAlign: 'right'
    }]
});
