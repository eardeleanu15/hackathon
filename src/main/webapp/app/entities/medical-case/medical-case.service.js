(function() {
    'use strict';
    angular
        .module('appApp')
        .factory('MedicalCase', MedicalCase);

    MedicalCase.$inject = ['$resource', 'DateUtils'];

    function MedicalCase ($resource, DateUtils) {
        var resourceUrl =  'api/medical-cases/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdon = DateUtils.convertLocalDateFromServer(data.createdon);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdon = DateUtils.convertLocalDateToServer(copy.createdon);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdon = DateUtils.convertLocalDateToServer(copy.createdon);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
