Ext.define('Portal.store.Cancellations', {
    extend:'Ext.data.Store',
    alias: 'store.cancellations',

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
                paramsAsJson: true,
                actionMethods: { read: 'POST' },
                url: Apps.Url.root + "/Cancellations/listview",
                reader: { type:'json', rootProperty:'list', totalProperty:'total' },
                headers: { 'Accept': 'application/json', 'Content-Type': 'application/json' }
            }
        },config)]);
    }
});
