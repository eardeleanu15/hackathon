(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('CaseFieldValueDetailController', CaseFieldValueDetailController);

    CaseFieldValueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CaseFieldValue', 'RegistryField', 'MedicalCase'];

    function CaseFieldValueDetailController($scope, $rootScope, $stateParams, previousState, entity, CaseFieldValue, RegistryField, MedicalCase) {
        var vm = this;

        vm.caseFieldValue = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('appApp:caseFieldValueUpdate', function(event, result) {
            vm.caseFieldValue = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
