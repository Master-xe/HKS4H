Ext.define('Portal.model.Catalogs', {
    extend:'Ext.data.Model',

    fields:
    [
        { name: 'entry', type: 'string' },
        { name: 'label', type: 'string' }
    ],

    idProperty: 'entry'
});
