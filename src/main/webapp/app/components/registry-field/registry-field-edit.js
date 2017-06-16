(function() {
    'use strict';

    function RegistryFieldEditCtrl($scope, $element, $attrs, Registry) {
        var ctrl = this;

        ctrl.fieldTypes = [
            {value: 'TEXT', label: 'Alphanumeric'},
            {value: 'NUMERIC', label: 'Numeric'},
            {value: 'DROPDOWN', label: 'Dropdown'},
            {value: 'MULTI_SELECT', label: 'Multi-select'},
            {value: 'CHECKBOX', label: 'Checkbox'},
            {value: 'FILE', label: 'Attached file'}
        ];

        ctrl.isSelect = function() {
            return (['DROPDOWN', 'MULTI_SELECT'].indexOf(ctrl.registryField.type) >= 0);
        };

        ctrl.canBeMandatory = function() {
            return (['DROPDOWN', 'CHECKBOX'].indexOf(ctrl.registryField.type) === -1);
        };

        ctrl.getDropdownOptions = function() {
            console.warn('-=-=-=-', ctrl.registryField);
            if (!ctrl.registryField.values) return [];
            var dropdownOptions = ctrl.registryField.values.split(',').map(function(str){ return str.trim(); });
            return dropdownOptions;
        };
    }

    RegistryFieldEditCtrl.$inject = ['$scope', '$element', '$attrs', 'Registry'];

    angular.module('appApp').component('registryFieldEdit', {
        templateUrl: 'app/components/registry-field/registry-field-edit.html',
        controller: RegistryFieldEditCtrl,
        bindings: {
            registryField: '=',
            removeField: '='
        }
    });
})();
