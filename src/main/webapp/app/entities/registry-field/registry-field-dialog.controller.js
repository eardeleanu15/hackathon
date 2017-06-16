(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('RegistryFieldDialogController', RegistryFieldDialogController);

    RegistryFieldDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RegistryField', 'Registry', 'CaseFieldValue'];

    function RegistryFieldDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RegistryField, Registry, CaseFieldValue) {
        var vm = this;

        vm.registryField = entity;
        vm.clear = clear;
        vm.save = save;
        vm.registries = Registry.query();
        vm.casefieldvalues = CaseFieldValue.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.registryField.id !== null) {
                RegistryField.update(vm.registryField, onSaveSuccess, onSaveError);
            } else {
                RegistryField.save(vm.registryField, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('appApp:registryFieldUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
