(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('MedicalCaseController', MedicalCaseController);

    MedicalCaseController.$inject = ['MedicalCase', '$http'];

    function MedicalCaseController(MedicalCase, $http) {

        var vm = this;

        vm.medicalCases = [];
        vm.user = {};

        loadAll();

        function loadAll() {
            MedicalCase.query(function(result) {
                vm.medicalCases = result;
                vm.searchQuery = null;
            });
        }

        $http.get('/api/users/current').then(function(result){
            vm.user = result.data;
        });
    }
})();
