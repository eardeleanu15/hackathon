(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('MedicalCaseDialogController', MedicalCaseDialogController);

    MedicalCaseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MedicalCase', 'Registry', 'CaseFieldValue'];

    function MedicalCaseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MedicalCase, Registry, CaseFieldValue) {
        var vm = this;

        vm.selectedRegistry = null;
        vm.medicalCase = entity;
        console.warn('vm.medicalCase', vm.medicalCase);
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        $scope.selectRegistry = selectRegistry;

        vm.registries = Registry.query();

        vm.casefieldvalues = CaseFieldValue.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {

            var registryFields = jQuery('.registry-field');
            vm.isSaving = true;
            vm.medicalCase.caseFields = [];
            // vm.medicalCase.registry = {id:vm.medicalCase.registry.id};

            jQuery.each(registryFields, function(idx, item) {
                var caseId = jQuery(item).data("fieldvalue-id") || jQuery(item).data("field-id");
                vm.medicalCase.caseFields.push({id: caseId, value:item.value ? item.value : jQuery(item)[0].checked});
            });
            if (vm.medicalCase.id !== null) {
                MedicalCase.update(vm.medicalCase, onSaveSuccess, onSaveError);
            } else {
                MedicalCase.save(vm.medicalCase, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('appApp:medicalCaseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdon = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function selectRegistry() {
            vm.selectedRegistry = vm.medicalCase.registry;
            // var selectedRegistry = Registry.get({id : vm.selectedRegistry.id});

        }
    }
})();
