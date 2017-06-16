(function() {
    'use strict';

    function RegistriesController($scope, $element, $attrs, Registry) {
        var ctrl = this;
        console.debug('sasa');
        ctrl.registries = [];

        loadAllActive();

        function loadAllActive() {
            Registry.query({active: true}, function(result) {
                ctrl.registries = result;
                ctrl.searchQuery = null;
                console.debug(ctrl.registries);
            });
        }
    }

    RegistriesController.$inject = ['$scope', '$element', '$attrs', 'Registry'];

    angular.module('appApp').component('registries', {
        templateUrl: 'app/components/registries/registries.html',
        controller: RegistriesController,
    });
})();
