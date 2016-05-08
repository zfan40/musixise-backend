'use strict';

describe('Controller Tests', function() {

    describe('WorkListFollow Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWorkListFollow, MockWorkList, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWorkListFollow = jasmine.createSpy('MockWorkListFollow');
            MockWorkList = jasmine.createSpy('MockWorkList');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'WorkListFollow': MockWorkListFollow,
                'WorkList': MockWorkList,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("WorkListFollowDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'musixiseApp:workListFollowUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
