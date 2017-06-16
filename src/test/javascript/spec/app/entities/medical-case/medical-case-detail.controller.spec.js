'use strict';

describe('Controller Tests', function() {

    describe('MedicalCase Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMedicalCase, MockRegistry, MockCaseFieldValue;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMedicalCase = jasmine.createSpy('MockMedicalCase');
            MockRegistry = jasmine.createSpy('MockRegistry');
            MockCaseFieldValue = jasmine.createSpy('MockCaseFieldValue');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MedicalCase': MockMedicalCase,
                'Registry': MockRegistry,
                'CaseFieldValue': MockCaseFieldValue
            };
            createController = function() {
                $injector.get('$controller')("MedicalCaseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'appApp:medicalCaseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
