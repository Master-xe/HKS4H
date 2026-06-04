Ext.define('Portal.store.Companies', {
    extend:'Ext.data.Store',
    alias: 'store.companies',

    constructor: function(config)
    {
        config = config || {};
        this.callParent([Ext.apply({
            model: 'Portal.model.Companies',
            autoDestroy: false,
            autoLoad: false,
            pageSize: 1000,
            proxy:
            {
                type: 'ajax',
                paramsAsJson: true,
                actionMethods: { read: 'POST' },
                url: Apps.Url.root + "/Companies/listview",
                reader: { type:'json', rootProperty:'list', totalProperty:'total' },
                headers: { 'Accept': 'application/json', 'Content-Type': 'application/json' }
            }
        },config)]);
    }
});
