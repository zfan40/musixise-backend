(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('WorkListFollowDetailController', WorkListFollowDetailController);

    WorkListFollowDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'WorkListFollow', 'WorkList', 'User'];

    function WorkListFollowDetailController($scope, $rootScope, $stateParams, entity, WorkListFollow, WorkList, User) {
        var vm = this;
        vm.workListFollow = entity;
        vm.load = function (id) {
            WorkListFollow.get({id: id}, function(result) {
                vm.workListFollow = result;
            });
        };
        var unsubscribe = $rootScope.$on('musixiseApp:workListFollowUpdate', function(event, result) {
            vm.workListFollow = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
