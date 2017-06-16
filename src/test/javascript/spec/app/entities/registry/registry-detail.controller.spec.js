'use strict';

describe('Controller Tests', function() {

    describe('Registry Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRegistry, MockRegistryField, MockMedicalCase;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRegistry = jasmine.createSpy('MockRegistry');
            MockRegistryField = jasmine.createSpy('MockRegistryField');
            MockMedicalCase = jasmine.createSpy('MockMedicalCase');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Registry': MockRegistry,
                'RegistryField': MockRegistryField,
                'MedicalCase': MockMedicalCase
            };
            createController = function() {
                $injector.get('$controller')("RegistryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'appApp:registryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
