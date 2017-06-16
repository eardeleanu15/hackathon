(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('RegistryController', RegistryController);

    RegistryController.$inject = ['Registry'];

    function RegistryController(Registry) {

        var vm = this;

        vm.registries = [];

        loadAll();

        function loadAll() {
            Registry.query(function(result) {
                vm.registries = result;
                vm.searchQuery = null;
            });
        }
    }
})();
