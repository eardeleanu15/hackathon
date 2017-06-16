(function() {
    'use strict';

    function RegistryFieldCtrl($scope, $element, $attrs, Registry, FileUploader, $cookies, $timeout) {
        var ctrl = this;

        ctrl.fieldTypes = [
            {value: 'TEXT', label: 'Alphanumeric'},
            {value: 'NUMERIC', label: 'Numeric'},
            {value: 'DROPDOWN', label: 'Dropdown'},
            {value: 'MULTI_SELECT', label: 'Multi-select'},
            {value: 'CHECKBOX', label: 'Checkbox'},
            {value: 'FILE', label: 'Attached file'}
        ];

        ctrl.getDropdownOptions = function() {
            if (!ctrl.registryField.values) return [];
            var dropdownOptions = ctrl.registryField.values.split(',').map(function(str){ return str.trim(); });
            return dropdownOptions;
        };

        // ctrl.
        var fileName = 'file_' + ctrl.medicalCaseId + '_' +ctrl.registryField.id+ '.png';
        ctrl.uploader = new FileUploader({
            url: '/api/case-field-values/attachment?medicalCaseId=' +
                ctrl.medicalCaseId+ '&registryFieldId=' +
                ctrl.registryField.id+ '&fileName=' + fileName,
            autoUpload: true,
            headers: {
                "X-XSRF-TOKEN": $cookies.get('XSRF-TOKEN')
            },
            onCompleteItem: function(item, response) {
                $timeout(function(){
                    ctrl.caseField.value = '';
                    $timeout(function(){
                        ctrl.caseField.value = fileName;
                    }, 100);
                });
            }
        });
    }

    RegistryFieldCtrl.$inject = ['$scope', '$element', '$attrs', 'Registry', 'FileUploader', '$cookies', '$timeout'];

    angular.module('appApp').component('registryField', {
        templateUrl: 'app/components/registry-field/registry-field.html',
        controller: RegistryFieldCtrl,
        bindings: {
            registryField: '=',
            caseField: '=',
            removeField: '=',
            medicalCaseId: '='
        }
    });
})();
