Ext.define('Portal.model.Cancellations', {
    extend:'Ext.data.Model',
    idProperty: 'docid',

    fields:
    [
        { name: 'docid', type: 'int' },
        { name: 'elapsed', type: 'int' },
        { name: 'ichosed', type: 'bool'},
        { name: 'date', type: 'string' },
        { name: 'updt', type: 'string' },
        { name: 'user', type: 'string' },
        { name: 'uuid', type: 'string' },
        { name: 'stage', type: 'string' },
        { name: 'receipt', type: 'string' },
        { name: 'response', type: 'string' },

        { name: 'rfce', type: 'string' },
        { name: 'rfcr', type: 'string' },
        { name: 'seal', type: 'string' },
        { name: 'folio', type: 'string' },
        { name: 'idate', type: 'string' },
        { name: 'serie', type: 'string' },
        { name: 'amount', type: 'number' },
        { name: 'doctype', type: 'string' },
        { name: 'currency', type: 'string' },

        { name: 'pstype', type: 'string' },
        { name: 'porder', type: 'string' },
        { name: 'kresult', type: 'string' },
        { name: 'kstatus', type: 'string' },
        { name: 'kmessage', type: 'string' },

        { name: 'augbl', type: 'string' },
        { name: 'belnr', type: 'string' },
        { name: 'blart', type: 'string' },
        { name: 'bstat', type: 'string' },
        { name: 'budat', type: 'string' },
        { name: 'bukrs', type: 'string' },
        { name: 'ktokk', type: 'string' },
        { name: 'lifnr', type: 'string' },
        { name: 'sgtxt', type: 'string' },
        { name: 'sstat', type: 'string' },
        { name: 'txtps', type: 'string' },
        { name: 'waers', type: 'string' },
        { name: 'xblnr', type: 'string' },
        { name: 'zlsch', type: 'string' },
        { name: 'ibelnr', type: 'string' },
        { name: 'iblart', type: 'string' },
        { name: 'isgtxt', type: 'string' }
    ]
});
