Ext.define('Portal.view.cancellations.CancellationsController', {
    extend:'Ext.app.ViewController',
    alias: 'controller.cancellations',

    onSearchPendings: function(source)
    {
        this.refreshStore(this.getView(), false);
    },

    onSearchProcessedes: function(source)
    {
        this.refreshStore(this.getView(), true);
    },

    onSelectDocument: function(source)
    {
        var vw = this.getView();
        var st = vw.getStore();
        var rs = st.data.length;
        var ln = "OK";

        for(i=0; i<rs; i++)
        {
            var record = st.getAt(i);

            if( record.get('ichosed') )
            {
                ln += '|' + record.get('docid') + ',' + record.get('uuid');
            }
        }

        Ext.MessageBox.show
        ({
            title: 'Confirmación',
            msg: 'Por favor confirme la operación.',
            icon: Ext.MessageBox.QUESTION,
            buttons: Ext.MessageBox.YESNO,
            fn: function(choice)
            {
                if( choice !== 'yes' ){ return; }

                vw.mask('Actualizando...');

                Ext.Ajax.request
                ({
                    url: Apps.Url.root + '/Cancellations/sapselect/' + ln,
                    scope: this,
                    encode: false,
                    method: 'POST',
                    timeout: 60000,
                    useDefaultXhrHeader: false,

                    success: function(response, options)
                    {
                        vw.unmask();
                        var result = Ext.decode(response.responseText);

                        if( result.code == 0 && result.flag == 0 )
                        {
                            st.load();
                        }   else
                        {
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
                        vw.unmask();

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
    },

    onExportExcel: function(source)
    {
        var processed = (source.itemId == 'yexportxlsx') ? true : false;
        this.excelRequest(this.getView(), processed);
    },

    onRespond: function(source, options)
    {
        var vw = this.getView();
        var st = vw.getStore();
        var rs = st.data.length;
        var me = this;
        var ln = "OK";

        for(i=0; i<rs; i++)
        {
            var record = st.getAt(i);
            var response = record.get('answer');

            if( response == 'Aceptacion' || response == 'Rechazo' )
            {
                ln += '|' + record.get('docid') + ',' + response;
            }
        }

        Ext.MessageBox.show
        ({
            title: 'Confirmación',
            msg: 'Por favor confirme la operación.',
            icon: Ext.MessageBox.QUESTION,
            buttons: Ext.MessageBox.YESNO,
            fn: function(choice)
            {
                if( choice !== 'yes' ){ return; }

                vw.mask('Actualizando...');

                Ext.Ajax.request
                ({
                    url: Apps.Url.root + '/Cancellations/update/' + ln,
                    scope: this,
                    encode: false,
                    method: 'POST',
                    timeout: 60000,
                    useDefaultXhrHeader: false,

                    success: function(response, options)
                    {
                        vw.unmask();
                        var result = Ext.decode(response.responseText);

                        if( result.code == 0 && result.flag == 0 )
                        {
                            me.refreshStore(vw, false);
                        }   else
                        {
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
                        vw.unmask();

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
    },

    onViewSAT: function(grid, row, column)
    {
        var record = grid.getStore().getAt(row);

        var rfce = record.get('rfce');
        var rfcr = record.get('rfcr');
        var seal = record.get('seal');
        var uuid = record.get('uuid');
        var total= record.get('amount');

        window.open('https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx?id=' + uuid + '&re=' + rfce + '&rr=' + rfcr + '&tt=' + total + '&fe=' + seal);
    },

    onViewXML: function(grid, row, column)
    {
        var record = grid.getStore().getAt(row);

        if( record.get('idate').length > 10 )
        {
            window.open(Apps.Url.root + '/bovedani/cfdis/' + record.get('uuid') + '.xml');
        }   else
        {
            Ext.MessageBox.show
            ({
                title: 'Advertencia!',
                msg: 'El documento parece no contar con el XML',
                icon: Ext.MessageBox.WARNING,
                buttons: Ext.MessageBox.OK
            });
        }
    },

    onViewReceipt: function(grid, row, column)
    {
        var record = grid.getStore().getAt(row);

        if( record.get('receipt') == 'Y' )
        {
            window.open(Apps.Url.root + '/bovedani/cfdis/' + record.get('uuid') + '-acuse.xml');
        }   else
        {
            Ext.MessageBox.show
            ({
                title: 'Advertencia!',
                msg: 'El documento aún no cuenta con el acuse de aceptación o rechazo',
                icon: Ext.MessageBox.WARNING,
                buttons: Ext.MessageBox.OK
            });
        }
    },

    refreshStore: function(view, processed)
    {
        var store = view.getStore();

        var startdateid = '#nstrtdate';
        var enddateid = '#nenddate';
        var entityid = '#ncmpnyid';
        var cfdiuuid='#ncfdiuuid';

        if( processed )
        {
            startdateid = '#ystrtdate';
            enddateid = '#yenddate';
            entityid = '#ycmpnyid';
            cfdiuuid='#ycfdiuuid';
        }

        var inidatext = view.down(startdateid).getValue();
        var endatext = view.down(enddateid).getValue();
        var companyid = view.down(entityid).getValue();
        var document = view.down(cfdiuuid).getValue();
        var inidate = new Date(inidatext);
        var endate = Ext.Date.add(new Date(endatext), Ext.Date.DAY, 1);
        var inidateformatted = Ext.Date.format(inidate, 'Y-m-d');
        var endateformatted = Ext.Date.format(endate, 'Y-m-d');

        store.proxy.extraParams = { cfdiuuid: document, company: companyid, replied: processed, startdate: inidateformatted, enddate: endateformatted };
        store.load();
    },

    excelRequest: function(view, processed)
    {
        var startdateid = '#nstrtdate';
        var enddateid = '#nenddate';
        var entityid = '#ncmpnyid';
        var cfdiuuid='#ncfdiuuid';

        if( processed )
        {
            startdateid = '#ystrtdate';
            enddateid = '#yenddate';
            entityid = '#ycmpnyid';
            cfdiuuid='#ycfdiuuid';
        }

        var inidatext = view.down(startdateid).getValue();
        var endatext = view.down(enddateid).getValue();
        var companyid = view.down(entityid).getValue();
        var document = view.down(cfdiuuid).getValue();
        var inidate = new Date(inidatext);
        var endate = Ext.Date.add(new Date(endatext), Ext.Date.DAY, 1);
        var inidateformatted = Ext.Date.format(inidate, 'Y-m-d');
        var endateformatted = Ext.Date.format(endate, 'Y-m-d');

        var fltrs = { company: companyid, replied: processed, startdate: inidateformatted, enddate: endateformatted };
        view.mask('Procesando...');

        Ext.Ajax.request
        ({
            url: Apps.Url.root + '/Cancellations/export',
            scope: this,
            encode: true,
            method: 'POST',
            timeout: 60000,
            jsonData: fltrs,
            useDefaultXhrHeader: false,
            headers: { 'Accept': 'application/json', 'Content-Type': 'application/json' },

            success: function(response, options)
            {
                var result = Ext.decode(response.responseText);

                if( result.code == 0 && result.flag == 0 )
                {
                    view.unmask();
                    window.open(Apps.Url.root + '/bovedani/reports/' + result.text + '.xlsx');
                }   else
                {
                    view.unmask();
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
                view.unmask();
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
}); // var link = document.createElement('a'); link.download = result.text; link.target = '_blank'; link.href = result.text; document.body.appendChild(link); link.click(); link.parentNode.removeChild(link);
