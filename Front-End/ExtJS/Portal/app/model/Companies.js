Ext.define('Portal.model.Companies', {
    extend:'Ext.data.Model',

    fields:
    [
        { name: 'cid', type: 'int' },
        { name: 'crfc', type: 'string' },
        { name: 'clock', type: 'string' },
        { name: 'ccode', type: 'string' },
        { name: 'cname', type: 'string' },
        { name: 'cdate', type: 'string' },
        { name: 'cupdt', type: 'string' }
    ],

    idProperty: 'cid'
});
