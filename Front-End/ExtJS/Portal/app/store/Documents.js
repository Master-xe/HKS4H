Ext.define('Portal.store.Documents', {
    extend:'Ext.data.Store',
    alias: 'store.documents',

    constructor: function(config)
    {
        config = config || {};
        this.callParent([Ext.apply({
            model: 'Portal.model.Cancellations',
            autoDestroy: false,
            autoLoad: false,
            pageSize: 10000,
            proxy:
            {
                type: 'ajax',
                paramsAsJson: false,
                actionMethods: { read: 'POST' },
                url: Apps.Url.root + "/Cancellations/saplist",
                reader: { type:'json', rootProperty:'list', totalProperty:'total' }
            }
        },config)]);
    }
});
