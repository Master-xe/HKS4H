Ext.define('Portal.view.companies.CompaniesController', {
    extend:'Ext.app.ViewController',
    alias: 'controller.companies',
    csdBase64: null,
    pfxBase64: null,

    onLoadCSDBase64: function(source, options)
    {
        csdBase64 = null;

        var items = document.getElementById(source.button.fileInputEl.id);

        if( items.files.length == 1 )
        {
            var fr = new FileReader();
            var fl = items.files[0];
            fr.onload = function(e){ csdBase64 = e.target.result; }
            fr.readAsDataURL(fl);
        }
    },

    onLoadPFXBase64: function(source, options)
    {
        pfxBase64 = null;

        var items = document.getElementById(source.button.fileInputEl.id);

        if( items.files.length == 1 )
        {
            var fr = new FileReader();
            var fl = items.files[0];
            fr.onload = function(e){ pfxBase64 = e.target.result; }
            fr.readAsDataURL(fl);
        }
    },

    onFileFilter: function(source)
    {
        source.fileInputEl.set({ accept: '.pfx' });
    },

    onDeleteCompany: function(grid, row, column)
    {
        var v = this.getView();
        var s = v.getStore();
        var record = grid.getStore().getAt(row);
        var ExtMessage = 'Esta seguro de eliminar el registro: <b>' + record.get('cname') + '</b>?';

        s.proxy.extraParams = { company: v.down('#scmpnyrfc').getValue() , status: "" };

        Ext.MessageBox.show
        ({
            title: 'Advertencia!',
            msg: ExtMessage,
            icon: Ext.MessageBox.WARNING,
            buttons: Ext.MessageBox.YESNO,
            fn: function(choise)
            {
                if( choise !== 'yes' ){ return; }

                v.mask('Eliminando...');
                s.remove(record);
                v.unmask();
                s.load();
            }
        });
    },

    onSearchCompany: function(source)
    {
        var v = this.getView(), s = v.getStore();
        var company = v.down('#scmpnyrfc').getValue();
        company = (company == '') ? null : company;
        s.proxy.extraParams = { crfc: company , status: "" };
        s.load();
    },

    onEnrollCompany: function(source, row, column)
    {
        var cframe = this.lookupReference('cformwindow');

        if(!cframe)
        {
            cframe = new Portal.view.companies.CompanyForm();
            this.getView().add(cframe);
        }

        var cform = this.lookupReference('companyform').getForm().getFields();
        var cmpny = Ext.ComponentQuery.query("#mgrcmpny")[0];

        if( source.itemId == undefined )
        { /*source.button.ownerCt.itemId*/
            var record = source.getStore().getAt(row);

            for(i=0; i<cform.items.length; i++)
            {   /*this.getView().down('#companyid')*/
                if( cform.items[i].itemId == 'companyid' )
                {
                    cform.items[i].setValue(record.get('cid'));
                }   else if( cform.items[i].itemId == 'icmpnyrfc' )
                {
                    cform.items[i].setValue(record.get('crfc'));
                    cform.items[i].setConfig('editable', false);
                }   else if( cform.items[i].itemId == 'icmpnycode' )
                {
                    cform.items[i].setValue(record.get('ccode'));
                    cform.items[i].setConfig('editable', false);
                }   else if( cform.items[i].itemId == 'icmpnyname' )
                {
                    cform.items[i].setValue(record.get('cname'));
                    cform.items[i].setConfig('editable', false);
                }   else if( cform.items[i].itemId == 'icmpnystat' )
                {
                    cform.items[i].setValue(record.get('clock'));
                }   else if( cform.items[i].itemId == 'ipfxfile' || cform.items[i].itemId == 'icsdfile' || cform.items[i].itemId == 'icpassword' || cform.items[i].itemId == 'ecpassword' )
                {
                    cform.items[i].setConfig('allowBlank', true);
                }
            }

            cframe.setTitle('Actualizar Entidad');
            cmpny.setText('Actualizar');
        }   else
        {
            this.lookupReference('companyform').getForm().reset();

            for(i=0; i<cform.items.length; i++)
            {
                if( cform.items[i].itemId == 'icmpnyrfc' || cform.items[i].itemId == 'icmpnycode' || cform.items[i].itemId == 'icmpnyname' )
                {
                    cform.items[i].setConfig('editable', true);
                }   else if( cform.items[i].itemId == 'ipfxfile' || cform.items[i].itemId == 'icsdfile' || cform.items[i].itemId == 'icpassword' || cform.items[i].itemId == 'ecpassword' )
                {
                    cform.items[i].setConfig('allowBlank', false);
                }
            }

            cframe.setTitle('Registrar Sociedad');
            cmpny.setText('Registrar');
        }

        cframe.show();
    },

    onCancelEnroll: function(source)
    {
        this.lookupReference('cformwindow').hide();
        this.lookupReference('companyform').getForm().reset();
    },

    onFormSubmit: function(source)
    {
        var cform = this.lookupReference('companyform').getForm();
        var cframe = this.lookupReference('cformwindow');
        var fvalues = cform.getValues();
        var v = this.getView();
        var s = v.getStore();

        var xaction = (fvalues.companyid == null || fvalues.companyid == '') ? 'enroll' : 'update';

        if(!cform.isValid())
        {
            var ExtMessage = 'Favor de verificar la información del formulario';
            Ext.MessageBox.show({ title: 'Error', msg: ExtMessage, icon: Ext.MessageBox.ERROR, buttons: Ext.MessageBox.OK }); return;
        }

        if( fvalues.icpassword != fvalues.ecpassword )
        {
            var ExtMessage = 'La contraseña no coincide';
            Ext.MessageBox.show({ title: 'Error', msg: ExtMessage, icon: Ext.MessageBox.ERROR, buttons: Ext.MessageBox.OK }); return;
        }

        v.mask('Procesando...');

        var ipfxBase64 = null;
        var icsdBase64 = null;

        try
        {
            icsdBase64 = csdBase64;
            ipfxBase64 = pfxBase64;
        }   catch(error){ console.log(error); }

        var cdata =
        {
            cid:    fvalues.companyid,
            crfc:   fvalues.icmpnyrfc,
            ccode:  fvalues.icmpnycode,
            clock:  fvalues.icmpnystat,
            cname:  fvalues.icmpnyname,
            cpswd:  fvalues.icpassword,
            rtype:  fvalues.idtreqtype,
            flcsd:  icsdBase64,
            flpfx:  ipfxBase64
        };

        Ext.Ajax.request
        ({
            url: Apps.Url.root + '/Companies/' + xaction,
            scope: this,
            encode: true,
            method: 'POST',
            timeout: 60000,
            jsonData: cdata,
            useDefaultXhrHeader: false,
            headers: { 'Accept': 'application/json', 'Content-Type': 'application/json' },

            success: function(response, options)
            {
                s.proxy.extraParams = { company: "", status: "" };
                var result = Ext.decode(response.responseText);

                if( result.code == 0 && result.flag == 0 )
                {
                    v.unmask();
                    cform.reset();
                    cframe.hide();
                    s.load();
                }   else
                {
                    v.unmask();
                    Ext.MessageBox.show
                    ({
                        title: result.type + " : " + result.code,
                        msg: result.text,
                        icon: Ext.MessageBox.ERROR,
                        buttons: Ext.MessageBox.OK
                    });
                }
            }, failure: function(response, options)
            {
                v.unmask();
                Ext.MessageBox.show
                ({
                    title: 'Error: ' + response.status,
                    msg: response.responseText,
                    icon: Ext.MessageBox.ERROR,
                    buttons: Ext.MessageBox.OK
                });
            }
        });
    }
});
