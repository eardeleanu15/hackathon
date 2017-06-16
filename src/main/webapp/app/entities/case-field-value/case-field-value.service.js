(function() {
    'use strict';
    angular
        .module('appApp')
        .factory('CaseFieldValue', CaseFieldValue);

    CaseFieldValue.$inject = ['$resource'];

    function CaseFieldValue ($resource) {
        var resourceUrl =  'api/case-field-values/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
