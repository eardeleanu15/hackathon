(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('CaseFieldValueController', CaseFieldValueController);

    CaseFieldValueController.$inject = ['CaseFieldValue'];

    function CaseFieldValueController(CaseFieldValue) {

        var vm = this;

        vm.caseFieldValues = [];

        loadAll();

        function loadAll() {
            CaseFieldValue.query(function(result) {
                vm.caseFieldValues = result;
                vm.searchQuery = null;
            });
        }
    }
})();
