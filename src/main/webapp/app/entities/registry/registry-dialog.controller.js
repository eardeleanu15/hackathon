(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('RegistryDialogController', RegistryDialogController);

    RegistryDialogController.$inject = ['$timeout', '$scope', '$state', 'entity', 'Registry'];

    function RegistryDialogController ($timeout, $scope, $state, entity, Registry) {
        var vm = this;

        vm.registry = entity;
        vm.defaultRegistryField = {
            active: true,
            mandatory: true,
            type: 'TEXT'
        };
        vm.activeStates = [
            {value: false, label: 'Inactive'},
            {value: true, label: 'Active'}
        ];
        vm.registryFields = [];
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.addField = addField;
        vm.removeField = removeField;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function addField(){
            var fieldModel = Object.assign({}, vm.defaultRegistryField);
            vm.registry.registryFields.push(fieldModel);
        }

        function removeField(field){
            if (field.id) return;
            var fieldIndex = vm.registry.registryFields.indexOf(field);
            if (fieldIndex >= 0) {
                $timeout(function(){
                    vm.registry.registryFields.splice(fieldIndex, 1);
                });
            }
        }

        function save () {
            vm.isSaving = true;
            if (vm.registry.id !== null) {
                Registry.update(vm.registry, onSaveSuccess, onSaveError);
            } else {
                Registry.save(vm.registry, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('appApp:registryUpdate', result);
            $state.go('registry', null, { reload: 'registry' });
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdon = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
