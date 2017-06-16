(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('CaseFieldValueDialogController', CaseFieldValueDialogController);

    CaseFieldValueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CaseFieldValue', 'RegistryField', 'MedicalCase'];

    function CaseFieldValueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CaseFieldValue, RegistryField, MedicalCase) {
        var vm = this;

        vm.caseFieldValue = entity;
        vm.clear = clear;
        vm.save = save;
        vm.registryfields = RegistryField.query();
        vm.medicalcases = MedicalCase.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.caseFieldValue.id !== null) {
                CaseFieldValue.update(vm.caseFieldValue, onSaveSuccess, onSaveError);
            } else {
                CaseFieldValue.save(vm.caseFieldValue, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('appApp:caseFieldValueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
