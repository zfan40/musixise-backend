(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('MusixiserFollowDetailController', MusixiserFollowDetailController);

    MusixiserFollowDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'MusixiserFollow'];

    function MusixiserFollowDetailController($scope, $rootScope, $stateParams, entity, MusixiserFollow) {
        var vm = this;
        vm.musixiserFollow = entity;
        vm.load = function (id) {
            MusixiserFollow.get({id: id}, function(result) {
                vm.musixiserFollow = result;
            });
        };
        var unsubscribe = $rootScope.$on('musixiseApp:musixiserFollowUpdate', function(event, result) {
            vm.musixiserFollow = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
