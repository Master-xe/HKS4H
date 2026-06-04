Ext.define('Portal.model.Users', {
    extend:'Ext.data.Model',

    fields:
    [
        { name: 'usrid', type: 'int' },
        { name: 'email', type: 'string' },
        { name: 'fname', type: 'string' },
        { name: 'login', type: 'string' },
        { name: 'uname', type: 'string' },
        { name: 'locked', type: 'string' },
        { name: 'logged', type: 'string' },
        { name: 'profile', type: 'string' }
    ],

    idProperty: 'usrid'
});
