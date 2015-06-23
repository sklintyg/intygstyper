var testData = {};
testData.cert = {
    id: '8eab08a6-e25a-44c1-8cf4-634df3b6459a',
    typ: 'TS_DIABETES_U06_V02',
    skapadAv: {
        personid: 'IFV1239877878-104B',
        fullstandigtNamn: 'Åsa Andersson',
        vardenhet: {
            enhetsid: 'IFV1239877878-1042',
            enhetsnamn: 'WebCert-Enhet1',
            postadress: 'Storgatan 1',
            postnummer: '12345',
            postort: 'Småmåla',
            telefonnummer: '0101234567890',
            vardgivare: {
                vardgivarid: 'IFV1239877878-1041',
                vardgivarnamn: 'WebCert-Vårdgivare1'
            }
        }
    },
    patient: {
        personid: '20121212-1212',
        fullstandigtNamn: 'Lilltolvan Tolvansson',
        fornamn: 'Lilltolvan',
        efternamn: 'Tolvansson',
        postadress: 'Storgatan 1',
        postnummer: '12345',
        postort: 'Småmåla'
    },
    intygAvser: {
        korkortstyp: [
            {type: 'AM', selected: false},
            {type: 'A1', selected: false},
            {type: 'A2', selected: false},
            {type: 'A', selected: false},
            {type: 'B', selected: false},
            {type: 'BE', selected: false},
            {type: 'TRAKTOR', selected: false},
            {type: 'C1', selected: false},
            {type: 'C1E', selected: false},
            {type: 'C', selected: false},
            {type: 'CE', selected: false},
            {type: 'D1', selected: false},
            {type: 'D1E', selected: false},
            {type: 'D', selected: false},
            {type: 'DE', selected: false},
            {type: 'TAXI', selected: false}
        ]
    },
    diabetes: {},
    hypoglykemier: {},
    syn: {},
    bedomning: {
        korkortstyp: [
            {type: 'AM', selected: false},
            {type: 'A1', selected: false},
            {type: 'A2', selected: false},
            {type: 'A', selected: false},
            {type: 'B', selected: false},
            {type: 'BE', selected: false},
            {type: 'TRAKTOR', selected: false},
            {type: 'C1', selected: false},
            {type: 'C1E', selected: false},
            {type: 'C', selected: false},
            {type: 'CE', selected: false},
            {type: 'D1', selected: false},
            {type: 'D1E', selected: false},
            {type: 'D', selected: false},
            {type: 'DE', selected: false},
            {type: 'TAXI', selected: false}
        ]
    },
    updateToAttic: function(model) {
    },
    restoreFromAttic: function() {
    }
};
