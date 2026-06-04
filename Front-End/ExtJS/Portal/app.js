Ext.define('Apps.Usr', { statics:{ jsession: null } });
Ext.define('Apps.Url', { statics:{ root: document.location.origin , index: document.URL } });
Ext.application({ name: 'Portal', models:['Cancellations','Catalogs','Companies','Users'], stores:['Cancellations','Catalogs','Companies','Users'], extend: 'Portal.Application' });
Ext.override(Ext.grid.View, { enableTextSelection: true });
