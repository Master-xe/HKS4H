Ext.define('Portal.view.home.Home', {
    extend:'Ext.Container',
    xtype: 'home',
    title: 'Home',
    layout:'center',

    requires:
    [
        'Ext.form.Panel'
    ],

    html: '<center>' +
            '<h1 style="padding-top: 50px" >' +
                'Sistema de Administración de'  +
            '</h1>' +
            '<h1>' +
                'Solicitud de Cancelaciones' +
            '</h1 style="padding-top: 50px" >' +
            '<br>' +
            '<img src="resources/images/logo.png" />' +
          '</center>'
});
