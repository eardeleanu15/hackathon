(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('CaseFieldValueDeleteController',CaseFieldValueDeleteController);

    CaseFieldValueDeleteController.$inject = ['$uibModalInstance', 'entity', 'CaseFieldValue'];

    function CaseFieldValueDeleteController($uibModalInstance, entity, CaseFieldValue) {
        var vm = this;

        vm.caseFieldValue = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CaseFieldValue.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
