(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('MedicalCaseDetailController', MedicalCaseDetailController);

    MedicalCaseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MedicalCase', 'Registry', 'CaseFieldValue'];

    function MedicalCaseDetailController($scope, $rootScope, $stateParams, previousState, entity, MedicalCase, Registry, CaseFieldValue) {
        var vm = this;

        vm.medicalCase = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('appApp:medicalCaseUpdate', function(event, result) {
            vm.medicalCase = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
