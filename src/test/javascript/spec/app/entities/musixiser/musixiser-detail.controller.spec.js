'use strict';

describe('Controller Tests', function() {

    describe('Musixiser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMusixiser, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMusixiser = jasmine.createSpy('MockMusixiser');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Musixiser': MockMusixiser,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("MusixiserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'musixiseApp:musixiserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
