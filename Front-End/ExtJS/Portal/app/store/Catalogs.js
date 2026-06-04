Ext.define('Portal.store.Catalogs', {
    extend:'Ext.data.Store',
    alias: 'store.catalogs',

    constructor: function(config)
    {
        config = config || {};
        this.callParent([Ext.apply({
            model: 'Portal.model.Catalogs',
            autoDestroy: false,
            autoLoad: false,
            pageSize: 1000,
            proxy:
            {
                type: 'ajax',
                actionMethods: { read: 'POST' },
                reader: { type:'json', rootProperty:'list' },
                url: Apps.Url.root + "/Catalogs/list?table=companies"
            }
        },config)]);
    }
});
